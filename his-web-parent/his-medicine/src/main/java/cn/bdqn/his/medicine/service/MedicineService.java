package cn.bdqn.his.medicine.service;

import java.util.List;

import cn.bdqn.his.medicine.entity.Medicine;

public interface MedicineService {
    List<Medicine> findAll();
    List<Medicine> findAll(Integer typeId,String name);
}
