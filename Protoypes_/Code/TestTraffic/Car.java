
class Car implements Runnable {
	private String name;
	private String status = "Moving";
	

	public Car(String name) {
		this.name = name;
	}

	public String getCarName() {
		return name;
	}
	
	public void setStatus(boolean flag){
		if(flag){
			status = "Car Stopped";
		}else{
			status = "Car Moving";
		}
	}
	
	public String getStatus(){
		
		return status;
	}

	public void run() {
         while (true) {
        	 System.out.println("WAITING...");
          try {Thread.sleep(200);} catch (Exception ex) {}
                }
    }

    

}