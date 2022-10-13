package com.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardDTO;
import com.board.service.BoardService;



@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping(value = "/board/write.do")
	public String openBoardWrite(@RequestParam(value="idx", required = false) Long idx, Model model) {
		if (idx == null) {
			model.addAttribute("board", new BoardDTO());
		} else {
			BoardDTO board = boardService.getBoardDetail(idx);
			if(board == null) {
				return "redirect:/board/list.do";
			}
			model.addAttribute("board", board);
		}
		return "board/write";
	}
	
	@PostMapping(value = "/board/register.do")
	public String registerBoard(final BoardDTO params) {
		try {
			boolean isRegistered = boardService.registerBoard(params);
			if( isRegistered == false ) {
				System.out.println("게시글 등록 실패");
			}
		} catch (DataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/board/list.do";
		
	}

	@GetMapping(value = "/board/list.do")
	public String openBoardList(Model model){
		List<BoardDTO> boardList = boardService.getBoardList();
		model.addAttribute("boardList", boardList);

		return "board/list";
	}


	@GetMapping(value="/board/view.do")
	public String openBoardDetail(@RequestParam (value= "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			System.out.println("올바르지 않은 접근 권한");
			return "redirect:/board/list.do";
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if(board == null || "Y".equals(board.getDeleteYn())){
			System.out.println("이미 삭제된 게시글");
			return "redirect:/board/list.do";
		}

		model.addAttribute("board", board);

		return "board/view";
	}
	
	
}
