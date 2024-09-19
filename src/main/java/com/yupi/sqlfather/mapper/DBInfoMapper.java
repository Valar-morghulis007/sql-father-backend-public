package com.yupi.sqlfather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.sqlfather.model.entity.DBInfo;
import com.yupi.sqlfather.model.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author https://github.com/liyupili
 * @description 针对表【dict(词库)】的数据库操作Mapper
 * @createDate 2022-10-04 22:53:51
 * @Entity com.yupi.sqlfather.model.entity.Dict
 */
@Mapper
public interface DBInfoMapper extends BaseMapper<DBInfo> {

}




