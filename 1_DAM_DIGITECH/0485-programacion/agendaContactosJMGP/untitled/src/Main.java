import Controlador.Controller;
import Vista.Vista;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Vista vista = new Vista();
        Controller controller = new Controller(vista);

        controller.Menu();
    }
}