package app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bean.CurrentPriceBean;
import app.dao.CurrentCodeDAO;
import app.dao.CurrentPriceDAO;

@Service
public class CurrentPriceService {

	@Autowired
	private CurrentPriceDAO cpDao;

	@Autowired
	private CurrentCodeDAO ccDao;
	
	public List<CurrentPriceBean> findAll() {
		List<CurrentPriceBean> cpList = (List<CurrentPriceBean>) cpDao.findAll();
		return cpList;
	}
	
	public CurrentPriceBean findByCurrent(String current) {
		System.out.println("----------");
		System.out.println(cpDao);
		return cpDao.findById(current).get();
	}
	
	public CurrentPriceBean addData(CurrentPriceBean cpBean) {
		return cpDao.save(cpBean);
	}
	
	public CurrentPriceBean update(CurrentPriceBean cpBean) {
		CurrentPriceBean updateCpBean = cpDao.findById(cpBean.getCode()).get();
		updateCpBean.setDescription(cpBean.getDescription());
		updateCpBean.setRate(cpBean.getRate());
		updateCpBean.setRate_float(cpBean.getRate_float());
		updateCpBean.setSymbol(cpBean.getSymbol());
		updateCpBean.setUpdated(cpBean.getUpdated());
		return cpDao.save(updateCpBean);
	}
	
	public CurrentPriceBean add(CurrentPriceBean cpBean) {
		CurrentPriceBean addCpBean = new CurrentPriceBean();
		addCpBean.setCode(cpBean.getCode());
		addCpBean.setDescription(cpBean.getDescription());
		addCpBean.setRate(cpBean.getRate());
		addCpBean.setRate_float(cpBean.getRate_float());
		addCpBean.setSymbol(cpBean.getSymbol());
		addCpBean.setUpdated(cpBean.getUpdated());
		return cpDao.save(addCpBean);
	}
	
	@Transactional
	public void delete(String current) {
		CurrentPriceBean updateCpBean = cpDao.findById(current).get();
		if (updateCpBean != null) {
			cpDao.delete(updateCpBean);
		} else {
			throw new RuntimeException("Record not found");
		}
	}
	
	public String getCountry(String current) {
		return ccDao.findById(current).get().getCountry();
	}
}
