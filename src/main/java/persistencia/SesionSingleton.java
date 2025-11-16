package persistencia;

// Va a guardar el usuario actual que esta manejando el gestor de documentos

public class SesionSingleton {

    private static SesionSingleton instance;
    private Usuario usuarioLogueado;

    private SesionSingleton() {}

    public static SesionSingleton getInstance() {
        if (instance == null) {
            instance = new SesionSingleton();
        }
        return instance;
    }

    // Método para establecer el usuario logueado
    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    // Método para obtener el usuario logueado
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
