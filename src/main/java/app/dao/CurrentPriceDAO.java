package app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.bean.CurrentPriceBean;

@Repository
public interface CurrentPriceDAO extends CrudRepository<CurrentPriceBean, String>{

}
