import controller.ControladorJogo;
import model.ModelFacade;

public class Main {
    public static void main(String[] args) {
        ModelFacade facade = new ModelFacade();
        new ControladorJogo(facade).iniciarJogo();
    }
}
