package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.enums.DictEnum;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.mapper.PatientMapper;
import com.atguigu.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-22
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    // 远程调用 openfeign
    @Resource
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllPatientByUserId(Long userId) {
        List<Patient> patientList = baseMapper.selectList(new QueryWrapper<Patient>().eq("user_id", userId));
        // 封装就诊人列表信息
        // 封装参数
        patientList.forEach(this::packPatient);
        return patientList;
    }

    @Override
    public Patient getPatientById(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }


    // Patient 对象里面其他参数封装
    private Patient packPatient(Patient patient) {
        // 根据证件类型编码，获取证件类型。获取联系人证件类型
        String certificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        // 获取省份信息
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        // 获取市信息
        String cityString = dictFeignClient.getName(patient.getCityCode());
        // 获取区域信息
        String districtString = dictFeignClient.getName(patient.getDistrictCode());

        // 将上面通过远程调用获取的信息，封装到 patient 中的 param 属性中
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);

        return patient;
    }
}
