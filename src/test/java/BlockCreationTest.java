import Tetris.Form;
import org.junit.jupiter.api.Test;

import static Tetris.Controller.makeText;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockCreationTest {

    @Test
    public void testBlockDistribution() {
        boolean colorBlindMode = false; // 일반 모드로 설정
        char[] difficultyLevels = {'E', 'H', 'N'}; // 각각의 난이도

        for (char difficultyLevel : difficultyLevels) {
            int totalBlocks = 1000000; // 총 선택 횟수
            int[] blockCount = new int[7]; // 각 블럭의 선택 횟수를 저장할 배열

            for (int i = 0; i < totalBlocks; i++) {
                Form form = makeText(colorBlindMode, difficultyLevel);
                String blockName = form.getName();
                switch (blockName) {
                    case "j":
                        blockCount[0]++;
                        break;
                    case "l":
                        blockCount[1]++;
                        break;
                    case "o":
                        blockCount[2]++;
                        break;
                    case "s":
                        blockCount[3]++;
                        break;
                    case "t":
                        blockCount[4]++;
                        break;
                    case "z":
                        blockCount[5]++;
                        break;
                    case "i":
                        blockCount[6]++;
                        break;
                }
            }

            double baseProbability = (double) totalBlocks / 7; // 7가지 블럭이므로
            double expectedProbabilityI;
            if (difficultyLevel == 'E') {
                expectedProbabilityI = baseProbability * 1.2; // 'i' 블럭의 선택 확률은 기본의 120%
                totalBlocks += baseProbability * 1.2;
            } else if (difficultyLevel == 'H') {
                expectedProbabilityI = baseProbability * 0.8; // 'i' 블럭의 선택 확률은 기본의 80%
            } else {
                expectedProbabilityI = baseProbability; // 'N' 난이도에서는 동일
            }

            double errorMargin = baseProbability * 0.05; // 5%의 오차범위
            for (int i = 0; i < blockCount.length; i++) {
                double expectedProbability;
                if (i == 6) {
                    expectedProbability = expectedProbabilityI;
                } else {
                    expectedProbability = baseProbability;
                }

                assertTrue(Math.abs(blockCount[i] - expectedProbability) <= errorMargin);
            }
        }
    }
}
