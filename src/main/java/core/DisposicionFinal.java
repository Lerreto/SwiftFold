package core;

public enum DisposicionFinal {
    CONSERVAR("Conservar"),
    TRANSFERIR("Transferir a Archivo"),
    ELIMINAR("Eliminar (fin de retención)");

    private final String label;
    DisposicionFinal(String label){ this.label = label; }

    public String label(){ return label; }
    public String toDb(){ return name(); }

    public static DisposicionFinal fromDb(String v){
        try { return DisposicionFinal.valueOf(v); } catch (Exception e) { return CONSERVAR; }
    }
    public static DisposicionFinal byLabel(String lbl){
        for (var d : values()) if (d.label.equalsIgnoreCase(lbl)) return d;
        return CONSERVAR;
    }
}
