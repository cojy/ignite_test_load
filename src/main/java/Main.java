import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.ignite.IgniteDataStreamer;


public class Main {

	private static int maxThread = 1;
	
	public static void main( String[] args ) throws IOException, InterruptedException
    {
		
		IgniteDataStreamer<Long, LNSData> stmr = Configuration.getInstance().igniteInstance().dataStreamer("LNSDataCache3");
		IgniteDataStreamer<Long, LNSData> stmr2 = Configuration.getInstance().igniteInstance().dataStreamer("LNSDataCache3");
		
		stmr.autoFlushFrequency(2000);
		//stmr2.autoFlushFrequency(5000);
		
		Thread t = null;
		for(int nbThread = 0;nbThread<maxThread; nbThread++) {
			InsertThread it = new InsertThread(stmr);
			
	    	t = new Thread(it, "it-"+nbThread);
	    	it.run();
		}
		
		/*
		Thread t2 = null;
		for(int nbThread = 0;nbThread<maxThread; nbThread++) {
			InsertThread it = new InsertThread(stmr2);
			
	    	t2 = new Thread(it, "it2-"+nbThread);
	    	t2.start();
		}
		//*/
    	//t.join();
    	System.out.println("it's done");
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
				lLNSData.add(new LNSData(null, null, null, 48.52, 2.14, 0.0));
			}
			
			int i = 0;
			
			while(i<100) {		
				i++;
				TreeMap<Long, LNSData> dataToSave = new TreeMap<>();            
				try {    
				    for (LNSData lnsData : lLNSData) {
				    	lnsData.setId(IDGenerator.getInstance().getID());
				        dataToSave.put(lnsData.getId(), lnsData);
				        //stmr.addData(lnsData.getId(), lnsData);
				    }
				    Thread.sleep(2000);
				    System.out.println("add-"+i);
				    stmr.addData(dataToSave);
				    stmr.flush();
				    System.out.println(i);
				}catch(Exception e) {
					System.err.println(e);
				}	
			}
		}
		
	}
	
}
