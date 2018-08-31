import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;


public class Main {

	private static int maxThread = 6;
	
	public static void main( String[] args ) throws IOException, InterruptedException
    {
		
		IgniteDataStreamer<Long, LNSData> stmr = Configuration.getInstance().igniteInstance().dataStreamer("LNSDataCache3");
		IgniteDataStreamer<Long, LNSData> stmr2 = Configuration.getInstance().igniteInstance().dataStreamer("LNSDataCache3");
		
		IgniteDataStreamer<Long, LNSData2> stmr3 = Configuration.getInstance().igniteInstance().dataStreamer("LNSDataCache2");
		
		stmr.autoFlushFrequency(2000);
		stmr2.autoFlushFrequency(2000);
		stmr3.autoFlushFrequency(5000);
		
		System.out.println("Inserting initial data");
		
		InsertInitialData(stmr3);
		
		System.out.println("Insertion is over");
		
		Map<String, List<LNSData2>> lExtractedData = extractData();
		
		ExecutorService execServ = Executors.newFixedThreadPool(maxThread);
		
		for(List<LNSData2> lData : lExtractedData.values()) {
			InsertThread2 t = new InsertThread2(stmr, lData);
			execServ.submit(t);
		}		
		
    	System.out.println("all threads submitted");
    	
    	execServ.shutdown();
    	execServ.awaitTermination(600, TimeUnit.SECONDS);

    	System.out.println("it's over");
    	
		stmr3.close();
    	stmr.close();
    	stmr2.close();
    	
    	System.out.println("All closed. Bye Bye");
    	
    	System.exit(0);
    }
	
	private static Map<String, List<LNSData2>> extractData() {
		IgniteCache<Long, LNSData2> cache = Configuration.getInstance().getIgnite().cache("LNSDataCache2");
		
		SqlQuery sql = new SqlQuery(LNSData2.class, " id > ?");

		Map<String, List<LNSData2>> extractedData = new HashMap<>();
		
		try (QueryCursor<Entry<Long, LNSData2>> cursor = cache.query(sql.setArgs(1))) {
		  for (Entry<Long, LNSData2> e : cursor) {
			  LNSData2 t = e.getValue();
			  List<LNSData2> lData = extractedData.get(t.getDevEUI());
			  if(lData == null) {
				  lData = new ArrayList<>();
				  extractedData.put(e.getValue().getDevEUI(), lData);
			  }
			  lData.add(t);
		  }		    
		}
		return extractedData;
	}

	private static void InsertInitialData(IgniteDataStreamer<Long, LNSData2> stmr) {
		List<LNSData2> lLNSData = new ArrayList<>();
		for(int j=0;j<500;j++) {
			lLNSData.add(new LNSData2("AABBCC"+ (int) (j/10), null, null, null, 48.52, 2.14, 0.0));
		}
		
		int i = 0;
		int j = 0;
		while(i<500) {			
			TreeMap<Long, LNSData2> dataToSave = new TreeMap<>();    
			i++;
			try {    
			    for (LNSData2 lnsData : lLNSData) {
			    	j++;
			    	lnsData.setId(IDGenerator.getInstance().getID());
			        dataToSave.put(lnsData.getId(), lnsData);
			        //stmr.addData(lnsData.getId(), lnsData);
			    }
			    //Thread.sleep(2000);
			    System.out.println("add-"+j);
			    stmr.addData(dataToSave);
			    //stmr.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}		
	}
	
	public static class InsertThread2 implements Runnable{

		private final IgniteDataStreamer<Long, LNSData> stmr;
		private final List<LNSData2> lData2;
		
		public InsertThread2(IgniteDataStreamer<Long, LNSData> stmr, List<LNSData2> lData) {
			this.stmr = stmr;
			this.lData2 = lData;
		}
		
		@Override
		public void run() {

			TreeMap<Long, LNSData> dataToSave = new TreeMap<>();
			
			IgniteCache<Long, LNSData2> cache = Configuration.getInstance().getIgnite().cache("LNSDataCache2");
			
			for(LNSData2 ld2 : lData2) {
				dataToSave.put(ld2.getId(), new LNSData(ld2));
				cache.remove(ld2.getId());
			}
			         
			try {				
			    stmr.addData(dataToSave);
			    //stmr.flush();
			    System.out.println("Inserting "+dataToSave.size()+" data");
			}catch(Exception e) {
				e.printStackTrace();
			}	
		
		}
	}

	public static class InsertThread implements Runnable{

		private final IgniteDataStreamer<Long, LNSData> stmr;
		
		public InsertThread(IgniteDataStreamer<Long, LNSData> stmr) {
			this.stmr = stmr;
		}
		
		@Override
		public void run() {
			
			List<LNSData> lLNSData = new ArrayList<>();
			for(int j=0;j<500;j++) {
				lLNSData.add(new LNSData("AABBCC"+ (int) (j/10), null, null, null, 48.52, 2.14, 0.0));
			}
			
			int i = 0;
			
			while(i<500) {		
				i++;
				TreeMap<Long, LNSData> dataToSave = new TreeMap<>();            
				try {    
				    for (LNSData lnsData : lLNSData) {
				    	lnsData.setId(IDGenerator.getInstance().getID());
				        dataToSave.put(lnsData.getId(), lnsData);
				        //stmr.addData(lnsData.getId(), lnsData);
				    }
				    //Thread.sleep(2000);
				    System.out.println("add-"+i);
				    stmr.addData(dataToSave);
				    //stmr.flush();
				    System.out.println(i);
				}catch(Exception e) {
					e.printStackTrace();
				}	
			}
		}
		
	}
	
}
