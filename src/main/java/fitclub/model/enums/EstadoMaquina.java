package fitclub.model.enums;

public enum EstadoMaquina {
    DISPONIBLE, EN_USO, MANTENIMIENTO;

    @Override
    public String toString() {
        return switch (this) {
            case DISPONIBLE    -> "Disponible";
            case EN_USO        -> "En uso";
            case MANTENIMIENTO -> "Mantenimiento";
        };
    }

    public String toSQL() { return name().toLowerCase(); }
}