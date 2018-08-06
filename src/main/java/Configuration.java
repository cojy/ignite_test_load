import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

public class Configuration {

	private static final Configuration instance = new Configuration();
	
	private final IgniteAtomicSequence seq;

	private final Ignite ignite;

	private Configuration() {
		this.ignite = igniteInstance();
		
		AtomicConfiguration acfg = new AtomicConfiguration();
		acfg.setAtomicSequenceReserveSize(5000);
		seq = ignite.atomicSequence("lnsSequenceGen2", acfg, 0, true);
	}

	public static Configuration getInstance() {
		return instance;
	}

	public Ignite getIgnite() {
		return ignite;
	}

	public Ignite igniteInstance() {
		IgniteConfiguration cfg = new IgniteConfiguration();

		// Setting some custom name for the node.
		cfg.setIgniteInstanceName("springDataNode");

		// Enabling peer-class loading feature.
		cfg.setPeerClassLoadingEnabled(false);

		/*
		 * Keep it for the errorManager
		 */
		DataRegionConfiguration drc = new DataRegionConfiguration();
        drc.setName("persistent");
        drc.setPersistenceEnabled(true);
		
		DataRegionConfiguration lnsDrc = new DataRegionConfiguration();
		lnsDrc.setName("lnscache");
		// lnsDrc.setMaxSize(1L * 5 * 1024 * 1024);
		lnsDrc.setPersistenceEnabled(true);

		DataStorageConfiguration dsCfg = new DataStorageConfiguration();
		dsCfg.setDataRegionConfigurations(drc, lnsDrc);

		cfg.setDataStorageConfiguration(dsCfg);

		/*********************************************************************************/
        /********************************** PERSISTENT **********************************/
        /*******************************************************************************/        
        CacheConfiguration<Long, LNSData> ccfg = new CacheConfiguration<Long, LNSData>("LNSDataCache3");        
        ccfg.setDataRegionName("persistent");
        ccfg.setIndexedTypes(Long.class, LNSData.class);
        ccfg.setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.DAYS, 360)));
        ccfg.setEagerTtl(true);  
        
        /***************************************************************************/
		
		cfg.setCacheConfiguration(ccfg);
		cfg.setConsistentId("data-node");

		cfg.setDiscoverySpi(getTcpDiscoverySpi());
		cfg.setCommunicationSpi(getTcpCommunicationSpi());

		Ignition.setClientMode(true);
		Ignite ignite = Ignition.getOrStart(cfg);
		// ignite.cluster().active(true);

		return ignite;
	}

	private TcpDiscoverySpi getTcpDiscoverySpi() {
		// Explicitly configure TCP discovery SPI to provide list of initial nodes
		// from the first cluster.
		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();

		// Initial local port to listen to.
		discoverySpi.setLocalPort(48501);

		// Changing local port range. This is an optional action.
		discoverySpi.setLocalPortRange(20);

		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

		// Addresses and port range of the nodes from the first cluster.
		// 127.0.0.1 can be replaced with actual IP addresses or host names.
		// The port range is optional.
		String lbs_database_host = "127.0.0.1";
		String lbs_database_ports = "47099..47101";
		ipFinder.setAddresses(Arrays.asList(lbs_database_host + ":" + lbs_database_ports));

		// Overriding IP finder.
		discoverySpi.setIpFinder(ipFinder);

		return discoverySpi;
	}

	private TcpCommunicationSpi getTcpCommunicationSpi() {
		// Explicitly configure TCP communication SPI by changing local port number for
		// the nodes from the first cluster.
		TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
		// commSpi.setMessageQueueLimit(50000);
		commSpi.setLocalPort(48101);

		return commSpi;
	}

	public long getLNSId() {
		return seq.incrementAndGet();
	}
}
