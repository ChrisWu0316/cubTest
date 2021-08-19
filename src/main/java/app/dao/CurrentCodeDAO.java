package app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.bean.CurrentCodeBean;

@Repository
public interface CurrentCodeDAO extends CrudRepository<CurrentCodeBean, String>{

}
