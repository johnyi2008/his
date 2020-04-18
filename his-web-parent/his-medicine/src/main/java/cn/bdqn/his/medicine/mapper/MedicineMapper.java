package cn.bdqn.his.medicine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.bdqn.his.medicine.entity.Medicine;

@Mapper
public interface MedicineMapper {
    List<Medicine> findAll();
    List<Medicine> findAllBy(@Param("typeId") Integer typeId, @Param("name") String name);
}
