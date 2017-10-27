package ${package_path}.service.impl;

import ${package_path}.service.${class_name}Service;
import ${package_path}.mapper.${class_name}Mapper;
import ${package_path}.domain.${class_name};
import com.rd.ifaes.core.base.service.*;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * ServiceImpl:${class_name}ServiceImpl
 * @author ${author}
 * @version 3.0
 * @date ${sysDate?date}
 */
@Service("${class_name?uncap_first}Service") 
public class ${class_name}ServiceImpl  extends BaseServiceImpl<${class_name}Mapper, ${class_name}> implements ${class_name}Service{
	
    //@Resource
    //private ${class_name}Mapper ${class_name?uncap_first}Mapper;

}