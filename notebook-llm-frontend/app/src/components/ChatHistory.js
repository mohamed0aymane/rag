import React from 'react';
import '../css/ChatHistory.css';

function ChatHistory({ conversations }) {
  // Grouper les messages par conversation (alternance USER/ASSISTANT)
  const groupedMessages = [];
  for (let i = 0; i < conversations.length; i += 2) {
    if (i + 1 < conversations.length) {
      groupedMessages.push({
        user: conversations[i],
        assistant: conversations[i + 1]
      });
    }
  }

  return (
    <div className="chat-history">
      <h3>Historique des conversations</h3>
      
      {groupedMessages.length === 0 ? (
        <div className="no-history">
          <p>Aucune conversation précédente</p>
          <p>Commencez par poser une question !</p>
        </div>
      ) : (
        <div className="conversations-list">
          {groupedMessages.map((conv, index) => (
            <div key={index} className="conversation">
              <div className="message user-message">
                <div className="message-header">
                  <span className="message-role">👤 Vous</span>
                </div>
                <div className="message-content">{conv.user.text}</div>
              </div>
              
              <div className="message assistant-message">
                <div className="message-header">
                  <span className="message-role">🤖 Assistant</span>
                </div>
                <div className="message-content">{conv.assistant.text}</div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ChatHistory;