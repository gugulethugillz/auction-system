package zw.co.cassavasmartech.auctionsystem.schedulers.processors.api;

import zw.co.cassavasmartech.auctionsystem.model.Winner;

import java.util.List;

public interface WinnerProcessor {
    List<Winner> determineWinners();
}
