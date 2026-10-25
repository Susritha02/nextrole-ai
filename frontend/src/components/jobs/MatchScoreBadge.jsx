import React from 'react';
import { Sparkles } from 'lucide-react';

export default function MatchScoreBadge({ score }) {
  const numScore = typeof score === 'number' ? Math.round(score) : 0;
  
  let matchClass = 'match-high';
  if (numScore < 60) {
    matchClass = 'match-low';
  } else if (numScore < 80) {
    matchClass = 'match-med';
  }

  return (
    <div className={`match-score-pill ${matchClass}`}>
      <Sparkles size={14} />
      <span>{numScore}% Match</span>
    </div>
  );
}
