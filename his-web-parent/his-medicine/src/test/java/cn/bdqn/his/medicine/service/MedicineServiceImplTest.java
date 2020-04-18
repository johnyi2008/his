package cn.bdqn.his.medicine.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.bdqn.his.medicine.entity.Medicine;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MedicineServiceImplTest {
    @Resource MedicineService medicineService;

    @Test
    public void findAll() {
        PageHelper.startPage(1,2);
        List<Medicine> list = medicineService.findAll();
        PageInfo<Medicine> pageInfo = new PageInfo<>(list);
        log.debug(pageInfo.toString());
        pageInfo.getList().forEach(medicine -> {
            log.debug(medicine.toString());
        });
    }
    @Test
    public void findAllBy() {
        PageHelper.startPage(2,2);
        List<Medicine> list = medicineService.findAll(1,"化");
        PageInfo<Medicine> pageInfo = new PageInfo<>(list);
        log.debug(pageInfo.toString());
        pageInfo.getList().forEach(medicine -> {
            log.debug(medicine.toString());
        });

    }
}