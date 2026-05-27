package fitclub.service;

import fitclub.dao.MembresiaDAO;
import fitclub.dao.PagoDAO;
import fitclub.model.Membresia;
import fitclub.model.Pago;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Capa de servicios para la generación de reportes de ingresos del gimnasio Fit Club.
 * Provee métodos para consultar pagos por período, agrupar ingresos por tipo de
 * membresía y por mes, y calcular totales para la toma de decisiones administrativas.
 *
 * @author Camilo Andrés Durán Baquero
 */
public class ReporteService {

    private final PagoDAO pagoDAO;
    private final MembresiaDAO membresiaDAO;

    /**
     * Constructor de ReporteService.
     *
     * @param pagoDAO      DAO de pagos para consultar transacciones.
     * @param membresiaDAO DAO de membresías para consultar estados.
     */
    public ReporteService(PagoDAO pagoDAO, MembresiaDAO membresiaDAO) {
        this.pagoDAO = pagoDAO;
        this.membresiaDAO = membresiaDAO;
    }

    /**
     * Retorna todos los pagos registrados entre dos fechas (inclusive).
     *
     * @param inicio Fecha de inicio del período.
     * @param fin    Fecha de fin del período.
     * @return Lista de pagos dentro del período.
     * @throws IllegalArgumentException si las fechas son nulas o el inicio es posterior al fin.
     */
    public List<Pago> obtenerPagosPorPeriodo(LocalDate inicio, LocalDate fin) {
        validarFechas(inicio, fin);
        return pagoDAO.listarTodos().stream()
                .filter(p -> !p.getFechaPago().isBefore(inicio) && !p.getFechaPago().isAfter(fin))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el ingreso total del período especificado.
     *
     * @param inicio Fecha de inicio del período.
     * @param fin    Fecha de fin del período.
     * @return Suma total de los montos de todos los pagos del período.
     */
    public double calcularTotalIngresos(LocalDate inicio, LocalDate fin) {
        return obtenerPagosPorPeriodo(inicio, fin).stream()
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    /**
     * Agrupa y suma los ingresos por tipo de membresía dentro del período.
     * Ejemplo: {"mensual" -> 240000.0, "trimestral" -> 180000.0}
     *
     * @param inicio Fecha de inicio del período.
     * @param fin    Fecha de fin del período.
     * @return Mapa con tipo de membresía como clave y total de ingresos como valor.
     */
    public Map<String, Double> ingresosPorTipoMembresia(LocalDate inicio, LocalDate fin) {
        validarFechas(inicio, fin);
        Map<String, Double> resultado = new LinkedHashMap<>();

        List<Pago> pagos = obtenerPagosPorPeriodo(inicio, fin);

        for (Pago pago : pagos) {
            // Buscar la membresía asociada al pago para obtener su tipo
            Membresia membresia = membresiaDAO.buscarPorId(pago.getIdPago());
            if (membresia != null) {
                String tipo = membresia.getTipo().toLowerCase();
                resultado.merge(tipo, pago.getMonto(), Double::sum);
            }
        }
        return resultado;
    }

    /**
     * Agrupa y suma los ingresos por mes dentro del período.
     * El mes se representa como "yyyy-MM" (ej. "2025-05").
     *
     * @param inicio Fecha de inicio del período.
     * @param fin    Fecha de fin del período.
     * @return Mapa ordenado con mes como clave y total de ingresos como valor.
     */
    public Map<String, Double> ingresosPorMes(LocalDate inicio, LocalDate fin) {
        validarFechas(inicio, fin);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return obtenerPagosPorPeriodo(inicio, fin).stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFechaPago().format(formatter),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Pago::getMonto)
                ));
    }

    /**
     * Cuenta el total de membresías activas (vigentes) a la fecha actual.
     *
     * @param cedulas Lista de cédulas de clientes a consultar.
     * @return Número de membresías vigentes.
     */
    public long contarMembresiasActivas(List<String> cedulas) {
        if (cedulas == null || cedulas.isEmpty()) {
            throw new IllegalArgumentException("La lista de cédulas no puede estar vacía.");
        }
        return cedulas.stream()
                .flatMap(cedula -> membresiaDAO.listarPorCliente(cedula).stream())
                .filter(Membresia::estaVigente)
                .count();
    }

    /**
     * Genera un resumen completo del período especificado.
     * Incluye total de ingresos, número de pagos, ingresos por mes
     * e ingresos por tipo de membresía.
     *
     * @param inicio Fecha de inicio del período.
     * @param fin    Fecha de fin del período.
     * @return Mapa con todas las métricas del reporte.
     */
    public Map<String, Object> generarResumen(LocalDate inicio, LocalDate fin) {
        validarFechas(inicio, fin);
        Map<String, Object> resumen = new LinkedHashMap<>();

        List<Pago> pagos = obtenerPagosPorPeriodo(inicio, fin);

        resumen.put("periodoInicio", inicio.toString());
        resumen.put("periodoFin", fin.toString());
        resumen.put("totalPagos", pagos.size());
        resumen.put("totalIngresos", calcularTotalIngresos(inicio, fin));
        resumen.put("ingresosPorMes", ingresosPorMes(inicio, fin));
        resumen.put("ingresosPorTipo", ingresosPorTipoMembresia(inicio, fin));

        return resumen;
    }

    /**
     * Valida que las fechas de un período sean correctas.
     *
     * @param inicio Fecha de inicio.
     * @param fin    Fecha de fin.
     * @throws IllegalArgumentException si alguna fecha es nula o el inicio es posterior al fin.
     */
    private void validarFechas(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
        }
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }
}