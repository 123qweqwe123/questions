获取配置文件参数方法  
    public String getproperties(String name) {
		Properties props = new Properties();
	    InputStream inputStream = null;
	    try {
	        inputStream = getClass().getResourceAsStream("/message.properties");
	        props.load(inputStream);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (inputStream != null) {
	            try {
	                inputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return props.getProperty(name);
	}