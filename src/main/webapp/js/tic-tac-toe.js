// setup 0: user  1: computer
var scoreCard = { 1: [0, 0], 2: [0, 0], 3: [0, 0], 4: [0, 0], 5: [0, 0], 6: [0, 0], 7: [0, 0], 8: [0, 0] };

var board = [null, null, null, null, null, null, null, null, null];
var trans = ['147','15','168','24','2578', '26', '348','35','367'];

$('.box').on('click', function() {
  if ($(this).attr('class').split(' ').length === 1) {
    // player
    $(this).addClass('p0');
    board[trans.indexOf($(this).attr('id'))] = 0;
    updateScoreCard($(this).attr('id'), 0, scoreCard);
    checkWin(0, scoreCard);
    // AI
    var maxScore = -1024;
    for (var i = 0; i < board.length; i++) {
      if (board[i] === null) {
        var newBoard = board.slice();
        newBoard[i] = 1;
        var score = minimax(newBoard, 1, 0);
        if (score > maxScore) {
          maxScore = score;
          var move = trans[i];
        }
      }
    }
    if (move) {
      updateScoreCard(move, 1, scoreCard);
      checkWin(1, scoreCard);
      board[trans.indexOf(move)] = 1;
      $('#' + move).addClass('p1');
    }
  }
})

var updateScoreCard = function(id, player, scoreCard) {
  for (var i = 0; i < id.length; i++) {
    scoreCard[id[i]][player] += 1;
  }
}

var checkWin = function(player, scoreCard) {
  for (var i = 1; i < 9; i++) {
    if (scoreCard[i][player] === 3) {
      $(".box").off("click");
      return 1;
  } else if (scoreCard[i][1-player] === 3) {
      $(".box").off("click");
      return 0;
    }
  }
  if (board.indexOf(null) === -1) {
    $(".box").off("click");
    return null;
  }
  return -1;
}

var heuristic = function(scoreCard, player) {
  var score = 0;
  for (var i = 1; i < 9; i++) {
    if (scoreCard[i][player] === 3) {
      score += 256;
    } else if (scoreCard[i][player] === 2 && scoreCard[i][1-player] === 0) {
      score += 8;
    } else if (scoreCard[i][player] === 1 && scoreCard[i][1-player] === 0) {
      score += 1;
    }
  }
  for (var i = 1; i < 9; i++) {
    if (scoreCard[i][1-player] === 3) {
      score -= 128;
    } else if (scoreCard[i][1-player] === 2 && scoreCard[i][player] === 0) {
      score -= 8;
    } else if (scoreCard[i][1-player] === 1 && scoreCard[i][player] === 0) {
      score -= 1;
    }
  }
  return score;
}

var evaluate = function(board, player) {
  var card = { 1: [0, 0], 2: [0, 0], 3: [0, 0], 4: [0, 0], 5: [0, 0], 6: [0, 0], 7: [0, 0], 8: [0, 0] };
  for (var i = 0; i < board.length; i++) {
    if (board[i] !== null) {
      for (var j = 0; j < trans[i].length; j++) {
        card[trans[i][j]][board[i]] += 1;
      }
    }
  }
  return heuristic(card, player);
}

var minimax = function(board, depth, player) {
  if (depth === 0 || board.indexOf(null) === -1) {
    return evaluate(board, player);
  }
  if (player === 1) {
    var bestScore = -1024;
    for (var i = 0; i < board.length; i++) {
      if (board[i] === null) {
        var newBoard = board.slice();
        newBoard[i] = player;
        score = minimax(newBoard, depth - 1, 0);
        bestScore = Math.max(bestScore, score);
      }
    }
    return bestScore;
  } else {
    var bestScore = 1024;
    for (var i = 0; i < board.length; i++) {
      if (board[i] === null) {
        var newBoard = board.slice();
        newBoard[i] = player;
        score = minimax(newBoard, depth - 1, 1);
        bestScore = Math.min(bestScore, score);
      }
    }
    return bestScore;
  }
}