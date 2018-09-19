package org.gb.sample.algo.lcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

class LCSComputer {

	private static final Logger LOGGER = Logger.getLogger(LCSComputer.class);

	static List<Character> computeLCS(int firstSeqIdx, int secondSeqIdx, List<Character> seq1, List<Character> seq2) {
		//LOGGER.info(String.format("firstSeqIdx: %d, secondSeqIdx: %d.", firstSeqIdx, secondSeqIdx));
		List<Character> lcs = null;

		if (firstSeqIdx == -1 || secondSeqIdx == -1) {
			lcs = new ArrayList<>();
		} else if (seq1.get(firstSeqIdx).charValue() == seq2.get(secondSeqIdx).charValue()) {
			lcs = computeLCS(firstSeqIdx - 1, secondSeqIdx - 1, seq1, seq2);
			lcs.add(seq1.get(firstSeqIdx));
		} else {
			List<Character> backtrackedLCS1 = computeLCS(firstSeqIdx, secondSeqIdx - 1, seq1, seq2);
			List<Character> backtrackedLCS2 = computeLCS(firstSeqIdx - 1, secondSeqIdx, seq1, seq2);
			lcs = (backtrackedLCS1.size() >= backtrackedLCS2.size()) ? backtrackedLCS1 : backtrackedLCS2;
		}

		return lcs;
	}

	static List<Character> findLCS(List<Character> seq1, List<Character> seq2) {
		Map<String, List<Character>> idxCombVsLCS = new HashMap<>();
		List<Character> lcs = findLCS(seq1.size() - 1, seq2.size() - 1, seq1, seq2, idxCombVsLCS);
		return lcs;
	}

	static List<Character> findLCS(int firstSeqIdx, int secondSeqIdx, List<Character> seq1, List<Character> seq2,
			Map<String, List<Character>> idxCombVsLCS) {
		List<Character> lcs = null;
		String idxComb = String.valueOf(firstSeqIdx).concat("-").concat(String.valueOf(secondSeqIdx));

		if (firstSeqIdx == -1 || secondSeqIdx == -1) {
			return new ArrayList<>();
		} else if (idxCombVsLCS.containsKey(idxComb)) {
			lcs = idxCombVsLCS.get(idxComb);
		} else if (seq1.get(firstSeqIdx).charValue() == seq2.get(secondSeqIdx).charValue()) {
			lcs = findLCS(firstSeqIdx - 1, secondSeqIdx - 1, seq1, seq2, idxCombVsLCS);
			lcs = new ArrayList<>(lcs);
			lcs.add(seq1.get(firstSeqIdx));
		} else {
			List<Character> backtrackedLCS1 = findLCS(firstSeqIdx, secondSeqIdx - 1, seq1, seq2, idxCombVsLCS);
			List<Character> backtrackedLCS2 = findLCS(firstSeqIdx - 1, secondSeqIdx, seq1, seq2, idxCombVsLCS);
			lcs = (backtrackedLCS1.size() >= backtrackedLCS2.size()) ? backtrackedLCS1 : backtrackedLCS2;
		}

		idxCombVsLCS.put(idxComb, lcs);

		return lcs;

	}
}
