package atguigu.admin.service;

import atguigu.admin.bean.City;


public interface CityService {

     City getById(Long id);

     void saveCity(City city);

}
