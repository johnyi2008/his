package cn.bdqn.his.authserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bdqn.his.authserver.domain.TbPermission;
import cn.bdqn.his.authserver.mapper.TbPermissionMapper;

@Service
public class TbPermissionService {

    @Autowired
    private TbPermissionMapper permissionMapper;

    public List<TbPermission> getByUserid(Long userId){

        return permissionMapper.queryByUserid(userId);

    }
}
