import React, { useState } from 'react';
import '../css/ChatInterface.css';

function ChatInterface({ onAskQuestion, isLoading }) {
  const [question, setQuestion] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (question.trim() && !isLoading) {
      await onAskQuestion(question);
      setQuestion('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  return (
    <div className="chat-interface">
      <form onSubmit={handleSubmit} className="question-form">
        <div className="input-group">
          <textarea
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Posez votre question ici..."
            rows="3"
            disabled={isLoading}
          />
          <button 
            type="submit" 
            disabled={!question.trim() || isLoading}
            className="submit-button"
          >
            {isLoading ? (
              <>
                <span className="spinner"></span>
                Envoi...
              </>
            ) : 'Envoyer'}
          </button>
        </div>
        <p className="input-hint">
          Appuyez sur Entrée pour envoyer, Maj+Entrée pour une nouvelle ligne
        </p>
      </form>
    </div>
  );
}

export default ChatInterface;