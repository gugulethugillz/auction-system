package zw.co.cassavasmartech.auctionsystem.schedulers.processors.api;

import zw.co.cassavasmartech.auctionsystem.model.Winner;

import java.util.List;

/**
 * Created by alfred on 07 October 2020
 */
public interface WinnerProcessor {
    List<Winner> determineWinners();
}
