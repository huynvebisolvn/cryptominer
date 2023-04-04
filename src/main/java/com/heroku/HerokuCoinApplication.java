package com.heroku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heroku.FBInitialize;

import jp.nyatla.minya.SingleMiningChief;
import jp.nyatla.minya.connection.IMiningConnection;
import jp.nyatla.minya.connection.StratumMiningConnection;
import jp.nyatla.minya.worker.CpuMiningWorker;
import jp.nyatla.minya.worker.IMiningWorker;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class HerokuCoinApplication {
	SingleMiningChief worker;

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	@RequestMapping("/stop")
	public String stop() {
		try {
			worker.stopMining();
		} catch (Exception e) {}
		return "index";
	}
	@RequestMapping("/minning")
	public String regex(@RequestParam(name = "url") String url, @RequestParam("name") String name,@RequestParam("pass") String pass,
			@RequestParam("key") String key,@RequestParam("herokucoinx") String herokucoinx,
			Model model) throws Exception {

		FirebaseApp firebaseApp = new FBInitialize().connet();
		FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance(firebaseApp);
		DatabaseReference ref = defaultDatabase.getReference();
		DatabaseReference server = ref.child("server/"+herokucoinx);
		Thread thread = new Thread() {
			public void run() {
				if ("fX@45&pN".equals(key)) {
					try {
						server.setValueAsync(1);
						
						int cpuProcessors = Runtime.getRuntime().availableProcessors();
						IMiningConnection mc = new StratumMiningConnection(url, name, pass);
						IMiningWorker imw = new CpuMiningWorker(cpuProcessors);
						worker = new SingleMiningChief(mc, imw);
						worker.startMining();
					} catch (Exception e) {
						server.setValueAsync(0);
					}
				}
			}
		};
		thread.start();
		return "index";
	}
	public static void main(String[] args) {
		SpringApplication.run(HerokuCoinApplication.class, args);
	}
}
