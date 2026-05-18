package org.example.aplicaciondeltiempo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controlador {

    @FXML
    private AnchorPane container;

    @FXML
    private TextField textoCiudad;

    @FXML
    private Label errorCiudad;
    @FXML
    private Label buscandoCiudad;
    @FXML
    private Label datosCiudad;

    @FXML
    public void BuscarTiempo() {
        String ciudad = textoCiudad.getText().trim();

        errorCiudad.setText("");
        buscandoCiudad.setText("");
        datosCiudad.setText("");

        if (ciudad.isEmpty()) {
            errorCiudad.setText("Por favor, ingrese una ciudad.");
            return;
        }

        buscandoCiudad.setText("Buscando el tiempo para " + ciudad + "...");

        try {
            Tiempo tiempo = new ApiTiempo().servicioApi(ciudad);

            String datos = "Ciudad: " + ciudad.toUpperCase() + "\n" +
                    "Temperatura: " + tiempo.getTemperatura() + " °C\n" +
                    "Humedad: " + tiempo.getHumedad() + " %\n" +
                    "Viento: " + tiempo.getViento() + " km/h\n" +
                    "Código del clima: " + tiempo.getWeatherCode();

            datosCiudad.setText(datos);
            buscandoCiudad.setText("");

            actualizarFondoSegunCodigo(tiempo.getWeatherCode());

        } catch (Exception e) {
            buscandoCiudad.setText("");
            errorCiudad.setText("No  se pudo obtener el tiempo");
        }
    }

    public void actualizarFondoSegunCodigo(int codigoClima) {
        String urlImagen = "";

        switch (codigoClima) {
            case 0:
            case 1:
                urlImagen = "url('https://thumbs.dreamstime.com/b/tarjeta-con-elementos-meteorol%C3%B3gicos-sol-lindo-de-dibujos-animados-tarjetas-infantiles-para-el-aprendizaje-ilustraci%C3%B3n-vectorial-271863952.jpg')";
                break;

            case 2:
            case 3:
                urlImagen = "url('https://i.pinimg.com/736x/da/b5/7c/dab57ce30bd8e1c6c251717e98fd1e0a.jpg')";
                break;

            case 45:
            case 48:
                urlImagen = "url('https://www.shutterstock.com/image-vector/illustration-cloud-mascot-floating-fog-260nw-1465609064.jpg')";
                break;

            case 51: case 53: case 55:
            case 56: case 57:
            case 61: case 63: case 65:
            case 66: case 67:
            case 80: case 81: case 82:
                urlImagen = "url('https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjPoO0neyjobx_nJFEJPr32sKjIOl1P7dWiA&s')";
                break;

            case 71: case 73: case 75:
            case 77:
            case 85: case 86:
                urlImagen = "url('https://i.pinimg.com/originals/fd/46/5d/fd465dcbd837b72335a242efe4d7551c.jpg')";
                break;

            case 95:
            case 96: case 99:
                urlImagen = " url('https://i.pinimg.com/originals/59/01/7c/59017c20bbb5fff48a6f7f496cebdf6e.gif')";
                break;

            default:
                urlImagen = "url('https://arche-ele.com/wp-content/uploads/2020/10/portada-clima.png')";
                break;
        }

        container.setStyle(
                "-fx-background-image: " + urlImagen + ";" +
                        "-fx-background-repeat: repeat;" +
                        "-fx-background-size: 400px;" +
                        "-fx-background-position: center center;"
        );
    }
}
