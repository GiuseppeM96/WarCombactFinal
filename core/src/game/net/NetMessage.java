package game.net;

public class NetMessage {
	public int code;
	public int x;
	public float y;
	public int dir;
	public int action;
	public String name;
	
	/**
	 * decodes a net message and save its content 
	 * @param mess Message that will be decoded
	 */
	public NetMessage(String mess) {
		
		String sAction="",sCode="",sX="",sY="",sDir="";
		char [] arrayLine = new char[mess.length()];
		mess.getChars(0, mess.length(), arrayLine, 0);
		int i = 0;
		for (; arrayLine[i]!=';';i++) {
			sAction+=arrayLine[i];
		}
		i++;
		for (; arrayLine[i]!=';';i++) {
			sCode+=arrayLine[i];
		}
		i++;
		for (; arrayLine[i]!=';';i++) {
			sX+=arrayLine[i];
		}
		i++;
		for (; arrayLine[i]!=';';i++) {
			sY+=arrayLine[i];
		}
		i++;
		for (; arrayLine[i]!=';';i++) {
			sDir+=arrayLine[i];
		}
		action=convert(sAction);
		x=convert(sX);
		if(action==5){
			name=sCode;
			y=convert(sY);
		}
		else{
			dir=convert(sDir);
			code=convert(sCode);
			if(action==0){
				y=convertToFloat(sY);
			}
			else y=convert(sY);
		}
	}
	
	/**
	 * Convert string to in
	 * @param sCode
	 * @return
	 */
	private int convert(String sCode) {
		char[] tmp =sCode.toCharArray();
		int result=0;
		for (int i = 0; i < tmp.length; i++) {
			result*=10;
			result+=tmp[i]-'0';
		}
		return result;
	}
	/**
	 * Convert string to float
	 * @param sCode
	 * @return
	 */
	private float convertToFloat(String sCode) {
		char[] tmp =sCode.toCharArray();
		float result=0;
		int intPart=0,decimalPart=0;
		int ord=1;
		boolean point=false;
		for (int i = 0; i < tmp.length; i++) {
			if(tmp[i]=='.')
				point=true;
			else if(!point){
				intPart*=10;
				intPart+=tmp[i]-'0';
			}
			else{
				ord*=10;
				decimalPart*=10;
				decimalPart+=tmp[i]-'0';
			}
		}
		result=(float)decimalPart/ord;
		return result+intPart;
	}
}
