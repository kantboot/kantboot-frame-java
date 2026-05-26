package com.kantboot.tool.ip.dao.repository;

import com.kantboot.tool.ip.domain.entity.ToolIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolIpRepository extends JpaRepository<ToolIp, Long> {

    ToolIp findFirstByIp(String ip);

}
