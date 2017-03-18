package org.mattreyuk.adtech.dal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.mattreyuk.adtech.domain.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import static java.sql.Types.INTEGER;
@Repository
public class AdtechDal {

    private final JdbcTemplate jdbcTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(AdtechDal.class);

    @Autowired
    public AdtechDal(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
    public List<Provider> findProviders(Integer user_id, Integer width, Integer height){
    	List<Provider> result=null;
			try {
				result = jdbcTemplate.query(
						"SELECT * FROM provider WHERE provider_id IN "+
						"(SELECT provider_id FROM provider_size_assoc WHERE "+
						"ad_size_id IN "+ 
						"(SELECT ad_size_id FROM ad_size WHERE width= ? AND height= ?)) "+
						"and provider_id IN "+ 
						"(SELECT provider_id FROM user_provider_assoc WHERE user_id IN "+
						"(SELECT user_id FROM user_size_assoc WHERE user_id= ? and ad_size_id IN "+
						"(SELECT ad_size_id FROM ad_size WHERE width= ? AND height= ?)))",
						new Object[] {width,height,user_id,width,height},
						new int[]{INTEGER,INTEGER,INTEGER,INTEGER,INTEGER},
						(rs, rowNum) ->{
							try{
							return Provider.builder().provider_id(rs.getInt("provider_id")).
									provider_name(rs.getString("provider_name")).
									url(new URL(rs.getString("url"))).build();
							}catch(MalformedURLException e){
								LOGGER.warn("provider_id {} has invalid url: {}",rs.getInt("provider_id"),rs.getString("url"));
								return null;
							}
						});
			} catch (DataAccessException e) {
				LOGGER.error("failed on provider query",e);
			}
   	
		return result;
    	
    }
}
