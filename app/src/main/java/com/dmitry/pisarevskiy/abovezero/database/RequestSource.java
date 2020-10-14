package com.dmitry.pisarevskiy.abovezero.database;

import java.util.ArrayList;
import java.util.List;

public class RequestSource {
    private final RequestDao requestDao;
    // Буфер с данными: сюда будем подкачивать данные из БД
    private List<Request> requests;

   public RequestSource(RequestDao requestDao){
       this.requestDao = requestDao;
    }

   // Получить все запросы
    public List<Request> getRequests(){
       // Если объекты еще не загружены, загружаем их.
        // Это сделано для того, чтобы не делать запросы к БД каждый раз
       if (requests==null){
            LoadRequests();
        }
       return requests;
    }

    public void LoadRequests(){
       requests=requestDao.getAllRequests();
    }

   // Получаем количество записей
    public long getCountRequests(){
       return requestDao.getCountRequests();
    }

   // Добавляем запрос
    public void addRequest(Request request){
       requestDao.insertRequest(request);
       // После изменения БД надо повторно прочесть данные из буфера
       LoadRequests();
    }
}
