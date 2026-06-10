package fitclub.model.enums;

public enum MetodoPago {
    EFECTIVO, TARJETA, TRANSFERENCIA;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public String toSQL() { return name().toLowerCase(); }
}