package client;

import app.controller.HouseController;
import app.domain.House;

public class Client {
    public static void main(String[] args) {

        // Создаём HouseController - и обращаемся к нему
        HouseController controller = new HouseController();

        // имитация http-запроса
        controller.save("White", "120.5", "4", "2010", "250000");
        controller.save("Brown", "85.0", "3", "2005", "180000");
        controller.save("Gray", "95.3", "3", "2012", "210000");

        // Запросим все дома
        System.out.println(controller.getAll());
        System.out.println();

        // Найдем по идентификатору 3
        System.out.println(controller.getById("3"));
        System.out.println();

        // Удаление
        controller.deleteById("2");
        System.out.println(controller.getAll());
    }
}
