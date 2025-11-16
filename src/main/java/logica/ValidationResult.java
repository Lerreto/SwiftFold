package logica;
import java.util.ArrayList;
import java.util.List;


/**
 * Esta clase se utiliza para mostrar pequeños mensajes sobre las operaciones que se vayan realizando.
 * success = true si la operacion fue exitosa.
 * messages = lista de mensajes de error o de confirmacion.
 * Puede cambiarse al momento de enlazar la logica del registro e inicio de sesion con la UI.
 */

public class ValidationResult {
    private boolean success;
    private List<String> messages;

    public ValidationResult() {
        this.success = true;
        this.messages = new ArrayList<>();
    }

    public ValidationResult(boolean success) {
        this.success = success;
        this.messages = new ArrayList<>();
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<String> getMessages() { return messages; }

    public void addMessage(String msg) {
        this.messages.add(msg);
        this.success = false; 
    }

    public void addMessages(List<String> msgs) {
        this.messages.addAll(msgs);
    }
}