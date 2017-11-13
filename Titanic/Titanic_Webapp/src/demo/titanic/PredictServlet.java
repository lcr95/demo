package demo.titanic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hex.genmodel.easy.*;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

/**
 * Servlet implementation class PredictServlet
 * @author lcr95
 */
@WebServlet("/PredictServlet")
public class PredictServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PredictServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EasyPredictModelWrapper survivorModel;
		GradientBoostingMachine rawSurvivorModel = new GradientBoostingMachine();
        survivorModel = new EasyPredictModelWrapper(rawSurvivorModel); 
		
		System.out.println("To out-put All the request parameters received from request - ");

		Enumeration enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
		 String paramName = (String)enParams.nextElement();
		 System.out.println(paramName+":"+request.getParameter(paramName));
		}
		
		RowData rowData = convertDataFromHttpRequest(request);
		BinomialModelPrediction prediction;
		
		try {
			prediction = survivorModel.predictBinomial(rowData);
			String result = prepareJsonResponse(prediction);
			System.out.print(result);
			response.getWriter().write(result);
		    response.setStatus(HttpServletResponse.SC_OK);
		    
		} catch (Exception e) {
			System.out.println(e.getMessage());
		    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
		}
		
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Convert the http request parameter to fit into a RowData object that used 
	 * for model to do prediction.
	 * @param request Http request that contain data
	 * @return RowData
	 */
	private RowData convertDataFromHttpRequest(HttpServletRequest request) {
		RowData data = new RowData();
		Map<String, String[]> pramMap = request.getParameterMap();
		for(Map.Entry<String, String[]> entry : pramMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			for(String value : values) {
				if(value.length() > 0) {
					switch(key) {
						case "age":
							processAgeData(value, data);
							break;
						case "cabin":
							processCabinData(value, data);
							break;
						case "family":
							processFamilyData(value, data);
							break;
						case "embarked":
							processEmbarkData(value, data);
							break;
						case "pclass":
							processPclassData(value, data);
							break;
						default:
							data.put(key, value);
							break;
					}

				}
			}
		}
		return data;
	}
	
	private String prepareJsonResponse(BinomialModelPrediction pred) {
		
		StringBuilder sb = new StringBuilder();
	    sb.append("{\n");
	    sb.append("  \"Survived\" : ").append(pred.label).append("\n");
	    sb.append("}\n");
	    if(pred.label == "1") {
	    	System.out.println("Prediction: Alive!");
	    } else {
	    	System.out.println("Prediction: Death!");
	    }
	    return sb.toString();
	}
	
	private void processEmbarkData(String embarked, RowData data) {
		switch(embarked) {
			case "C":
				data.put("Embarked_C", "1");
				data.put("Embarked_S", "0");
				data.put("Embarked_Q", "0");
				break;
			case "S":
				data.put("Embarked_C", "0");
				data.put("Embarked_S", "1");
				data.put("Embarked_Q", "0");
				break;
			default :
				data.put("Embarked_C", "0");
				data.put("Embarked_S", "0");
				data.put("Embarked_Q", "1");
				break;
		}
	}
	
	private void processCabinData(String cabin, RowData data) {
		String cabin_class = cabin.substring(0, 1);
		switch(cabin_class) {
			case "A":
				data.put("Cabin_A", "1");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "B":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "1");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "C":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "1");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "D":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "1");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "E":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "1");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "F":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "1");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "G":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "1");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			case "T":
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "0");
				break;
			default:
				data.put("Cabin_A", "0");
				data.put("Cabin_B", "0");
				data.put("Cabin_C", "0");
				data.put("Cabin_D", "0");
				data.put("Cabin_E", "0");
				data.put("Cabin_F", "0");
				data.put("Cabin_G", "0");
				data.put("Cabin_T", "0");
				data.put("Cabin_U", "1");
				break;
		}	
	}

	private void processFamilyData(String family, RowData data) {
		int size = Integer.parseInt(family);
		if(size > 7) {
			data.put("LargeFamily", "1");
			data.put("MediumFamily", "0");
			data.put("SmallFamily", "0");
			data.put("Single", "0");
		} else if (size > 4 && size <= 7) {
			data.put("LargeFamily", "0");
			data.put("MediumFamily", "1");
			data.put("SmallFamily", "0");
			data.put("Single", "0");
		} else if (size > 1 && size <=4) {
			data.put("LargeFamily", "0");
			data.put("MediumFamily", "0");
			data.put("SmallFamily", "1");
			data.put("Single", "0");
		} else {
			data.put("LargeFamily", "0");
			data.put("MediumFamily", "0");
			data.put("SmallFamily", "0");
			data.put("Single", "1");
		}
	}
	
	private void processAgeData(String age, RowData data) {
		int Age = Integer.parseInt(age);
		if (Age >= 60 ) {
			data.put("Child", "0");
			data.put("Adult", "0");
			data.put("Eldery", "1");
		} else if (Age >= 18 && Age < 60) {
			data.put("Child", "0");
			data.put("Adult", "1");
			data.put("Eldery", "0");
		} else {
			data.put("Child", "1");
			data.put("Adult", "0");
			data.put("Eldery", "0");
		}
	}

	private void processPclassData(String pclass, RowData data) {
		switch (pclass){
			case "1":
				data.put("Pclass_1", "1");
				data.put("Pclass_2", "0");
				data.put("Pclass_3", "0");
				break;
			case "2":
				data.put("Pclass_1", "0");
				data.put("Pclass_2", "1");
				data.put("Pclass_3", "0");
				break;
			case "3":
				data.put("Pclass_1", "0");
				data.put("Pclass_2", "0");
				data.put("Pclass_3", "1");
				break;
			default:
				break;
		}
	}
}
