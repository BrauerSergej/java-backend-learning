package app.controller;

import app.domain.House;
import app.service.HouseService;
import app.service.HouseServiceImpl;

import java.math.BigDecimal;

// Имитация контроллера
// Контроллер все принимает через интернет в виде текста. Дальше задача контроллера — все преобразовать в нужные типы,
// создать нужные объекты и нужные объекты уже отдать сервису. А когда сервис возвращает ответ в виде опять-таки
// Java-объекта, то это надо все преобразовать снова в текст и опять текстом же отправить клиенту, потому что через
// интернет отправка информации возможна только текстом.

public class HouseController {

    private final HouseService service = new HouseServiceImpl();

    public String save(String color, String area, String rooms, String year, String price) {
        double numericArea = Double.parseDouble(area);
        int numericRooms = Integer.parseInt(rooms);
        int numericYear = Integer.parseInt(year);
        BigDecimal numericPrice = new BigDecimal(price);
        House house = new House(color, numericArea, numericRooms, numericYear, numericPrice);
        // Без конструктора
//        house.setColor(color);
//        house.setArea(numericArea);
//        house.setRooms(numericRooms);
//        house.setYear(numericYear);
//        house.setPrice(numericPrice);
        return service.save(house).toString();
    }

    public String getAll(){
        return service.getAll().toString();
    }

    public String getById(String id){
        Long numericId = Long.parseLong(id);
        return service.getById(numericId).toString();
    }

    public void deleteById(String id){
        Long numericId = Long.parseLong(id);
        service.deleteById(numericId);
    }
}
