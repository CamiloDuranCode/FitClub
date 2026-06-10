package fitclub.model.enums;

public enum TipoMembresia {
    MENSUAL, TRIMESTRAL, SEMESTRAL, ANUAL;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public String toSQL() { return name().toLowerCase(); }
}