package core;

public enum TipoAcceso {
    PUBLICO("PÚBLICO"),
    INTERNO("INTERNO"),
    RESERVADO("RESERVADO");

    private final String label;
    TipoAcceso(String label){ this.label = label; }

    public String label(){ return label; }      // para mostrar en UI
    public String toDb(){ return name(); }      // para guardar en BD

    public static TipoAcceso fromDb(String v){
        try { return TipoAcceso.valueOf(v); } catch (Exception e) { return INTERNO; }
    }
    public static TipoAcceso byLabel(String lbl){
        for (var t : values()) if (t.label.equalsIgnoreCase(lbl)) return t;
        return INTERNO;
    }
}
