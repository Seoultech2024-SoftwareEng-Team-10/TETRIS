package Test;

public class BlockPercentageTest {
    public static void main(String[] args) {
        int j = 0;
        int l = 0;
        int o = 0;
        int s = 0;
        int t = 0;
        int z = 0;
        int i = 0;
        char difficultyLevel = 'N';
        System.out.println(difficultyLevel);
        for(int k = 0;k<1000;k++){
            char testBlock = makeText(true,difficultyLevel);
            switch(testBlock) {
                case 'j' : j++; break;
                case 'l' : l++; break;
                case 'o' : o++; break;
                case 's' : s++; break;
                case 't' : t++; break;
                case 'z' : z++; break;
                case 'i' : i++; break;
            }
        }
        testPrint(j,l,o,s,t,z,i);
        j = 0;l = 0;o = 0;s = 0;t = 0;z = 0;i = 0;

        difficultyLevel = 'H';
        System.out.println(difficultyLevel);
        for(int k = 0;k<1000;k++){
            char testBlock = makeText(true,difficultyLevel);
            switch(testBlock) {
                case 'j' : j++; break;
                case 'l' : l++; break;
                case 'o' : o++; break;
                case 's' : s++; break;
                case 't' : t++; break;
                case 'z' : z++; break;
                case 'i' : i++; break;
            }
        }
        testPrint(j,l,o,s,t,z,i);
        j = 0;l = 0;o = 0;s = 0;t = 0;z = 0;i = 0;

        difficultyLevel = 'E';
        System.out.println(difficultyLevel);
        for(int k = 0;k<1000;k++){
            char testBlock = makeText(true,difficultyLevel);
            switch(testBlock) {
                case 'j' : j++; break;
                case 'l' : l++; break;
                case 'o' : o++; break;
                case 's' : s++; break;
                case 't' : t++; break;
                case 'z' : z++; break;
                case 'i' : i++; break;
            }
        }
        testPrint(j,l,o,s,t,z,i);
        j = 0;l = 0;o = 0;s = 0;t = 0;z = 0;i = 0;
    }
    public static void testPrint(int j, int l, int o, int s, int t, int z,int i){
        System.out.printf("j: %d ",j);
        System.out.printf("l: %d ",l);
        System.out.printf("o: %d ",o);
        System.out.printf("s: %d ",s);
        System.out.printf("t: %d ",t);
        System.out.printf("z: %d ",z);
        System.out.printf("i: %d \n",i);
    }
    public static char makeText(boolean colorBlindMode, char difficultyLevel) {
        int block = 0;
        if(difficultyLevel == 'E'){
            block = (int) (Math.random() * 72);

        }else if(difficultyLevel == 'H'){
            block = (int) (Math.random() * 68);

        }else{
            block = (int) (Math.random() * 70);
        }
        if(block<10)
            return 'j';
        if(block<20)
            return 'l';
        if(block<30)
            return 'o';
        if(block<40)
            return 's';
        if(block<50)
            return 't';
        if(block<60)
            return 'z';
        return 'i';
    }
}
