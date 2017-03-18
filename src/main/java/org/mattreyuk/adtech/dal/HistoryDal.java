package org.mattreyuk.adtech.dal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class HistoryDal {

	@Value("${history.size:100}")
	int size;

	private LinkedHashMap<UUID, Transaction> history;

	public HistoryDal() {
		history = new LinkedHashMap<UUID, Transaction>(size, (float) 0.75, false) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<UUID, Transaction> eldest) {
				return size() > size;
			}
		};
	};

	public void updateClickStatus(UUID id, ClickResult cr) {

		if (history.containsKey(id)) {
			history.compute(id, (k, v) -> v.withClickResult(cr));
		}

	}
	
	public void addTransaction(Transaction t){
		history.put(t.getTransactionId(), t);
	}
	
	public LocalDateTime getTransactionTime(UUID id){
		if(history.containsKey(id)){
			return history.get(id).getAdTime();
		}
		return null;
	}
	public List<Transaction> getCurrentHistory(){
		
		return new ArrayList<Transaction>(history.values());

	}

}
