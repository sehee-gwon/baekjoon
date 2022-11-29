package coding.company;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

class Miridi {
    public static void main(String[] args) {
        String[] records = {"2020-02-02 uid1 pid1", "2020-02-26 uid1 pid1", "2020-02-26 uid2 pid1", "2020-02-27 uid3 pid2", "2020-02-28 uid4 pid2", "2020-02-29 uid3 pid3", "2020-03-01 uid4 pid3", "2020-03-03 uid1 pid1", "2020-03-04 uid2 pid1", "2020-03-05 uid3 pid2", "2020-03-05 uid3 pid3", "2020-03-05 uid3 pid3", "2020-03-06 uid1 pid4"};
        int k = 10;
        String date = "2020-03-05";

        System.out.println(Arrays.toString(solution(records, k, date)));
    }

    /**
     1. 날짜 기준 구하기 (date 포함한 k 기간 이내)
     2. 날짜 기준을 충족하는 데이터 필터링
     3. 각 상품별 재구매율, 총 구매 횟수 구하기
     4. 재구매율 내림차순, 총 구매 횟수 내림차순, 상품 아이디 오름차순
     5. 빈 배열이면 ["no result"] 리턴하기
     **/
    public static String[] solution(String[] records, int k, String date) {
        Period period = new Period(date, k-1);
        List<Buy> buys = new ArrayList<>();

        for (String record : records) {
            Buy buy = new Buy(record.split(" "));
            if (period.valid(buy)) buys.add(buy);
        }

        if (buys.isEmpty()) return new String[]{"no result"};

        // 각 상품별 구매고객과 구매건수 그룹화
        Map<String, Map<String, Long>> map = buys.stream()
                .collect(groupingBy(Buy::getPid, groupingBy(Buy::getUid, mapping(Buy::getUid, counting()))));

        List<BuyStatistic> buyStatistics = new ArrayList<>();

        for (Map.Entry<String, Map<String, Long>> product : map.entrySet()) {
            int reBuyCount = 0;
            int buyTotal = 0;
            for (Map.Entry<String, Long> users : product.getValue().entrySet()) {
                buyTotal += users.getValue();
                if (users.getValue() > 1) reBuyCount++;
            }
            double reBuyRate = ((double) reBuyCount / product.getValue().size()) * 100;
            buyStatistics.add(new BuyStatistic(product.getKey(), reBuyRate, buyTotal));
        }

        // 재구매율 내림차순, 총 구매 횟수 내림차순, 상품 아이디 오름차순
        String[] answer = buyStatistics.stream()
                .sorted(
                        comparing(BuyStatistic::getReBuyRate, Comparator.reverseOrder())
                                .thenComparing(BuyStatistic::getBuyTotal, Comparator.reverseOrder())
                                .thenComparing(o -> Integer.parseInt(o.getPid().replace("pid", "")))
                )
                .map(BuyStatistic::getPid)
                .toArray(String[]::new);

        return answer;
    }

    static class Period {
        private String start;
        private String end;

        public Period(String date, int k) {
            String[] dateArray = date.split("\\-");
            int month = Integer.parseInt(dateArray[1]) - (k / 30);
            int day = Integer.parseInt(dateArray[2]) - (k % 30);

            if (day <= 0) {
                day += 30;
                month -= 1;
            }

            if (month <= 0) {
                month = 1;
                day = 1;
            }

            this.start = this.getDate(dateArray[0], month, day);
            this.end = date;
        }

        public String getDate(String year, int month, int day) {
            StringBuilder sb = new StringBuilder(year);
            sb.append("-");
            if (month < 10) sb.append("0");
            sb.append(month);
            sb.append("-");
            if (day < 10) sb.append("0");
            sb.append(day);
            return sb.toString();
        }

        public boolean valid(Buy buy) {
            return buy.getDate().compareTo(this.start) >= 0 && buy.getDate().compareTo(this.end) <= 0;
        }
    }

    static class Buy {
        private String date;
        private String uid;
        private String pid;

        public Buy(String[] recordArray) {
            this.date = recordArray[0];
            this.uid = recordArray[1];
            this.pid = recordArray[2];
        }

        public String getDate() {
            return this.date;
        }

        public String getUid() {
            return this.uid;
        }

        public String getPid() {
            return this.pid;
        }
    }

    static class BuyStatistic {
        private String pid;
        private double reBuyRate;
        private int buyTotal;

        public BuyStatistic(String pid, double reBuyRate, int buyTotal) {
            this.pid = pid;
            this.reBuyRate = reBuyRate;
            this.buyTotal = buyTotal;
        }

        public String getPid() {
            return this.pid;
        }

        public double getReBuyRate() {
            return this.reBuyRate;
        }

        public int getBuyTotal() {
            return this.buyTotal;
        }
    }
}