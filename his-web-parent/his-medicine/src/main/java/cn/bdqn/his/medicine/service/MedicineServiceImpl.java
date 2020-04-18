package cn.bdqn.his.medicine.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bdqn.his.medicine.entity.Medicine;
import cn.bdqn.his.medicine.mapper.MedicineMapper;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Resource
    private MedicineMapper medicineMapper;
    @Override
    public List<Medicine> findAll() {
        return medicineMapper.findAll();
    }

    @Override
    public List<Medicine> findAll(Integer typeId, String name) {
        return medicineMapper.findAllBy(typeId, name);
    }
}
