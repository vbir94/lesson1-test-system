package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LuckyTicket {

    public Long luckyTicketQty(int n) {
        List<Long> temp = Arrays.asList(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L);
        for (int i = 0; i < n - 1 ; i++) {
            temp = getNextList(temp);
        }
        return temp.stream().map(c -> c*c).collect(Collectors.summingLong(Long::longValue));
    }

    private List<Long> getNextList(List<Long> prevList){
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < prevList.size()+9; i++) {
            long q = 0;
            for (int j = 0; j < 10; j++) {
                if (i - j < prevList.size() && i -j >= 0) {
                    q += prevList.get(i-j);
                }
            }
            result.add(q);
        }
        return result;
    }
}
