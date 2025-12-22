import React from 'react';
import '../css/Header.css';

function Header({ activeTab, onTabChange, uploadedFiles }) {
  return (
    <header className="header">
      <div className="header-title">
        <h1>Notebook LLM Assistant</h1>
        <p>Chat intelligent basé sur vos documents PDF</p>
      </div>
      
      <nav className="nav-tabs">
        <button 
          className={`tab-button ${activeTab === 'chat' ? 'active' : ''}`}
          onClick={() => onTabChange('chat')}
        >
          💬 Chat
          {uploadedFiles.length > 0 && (
            <span className="file-count">{uploadedFiles.length} PDF(s)</span>
          )}
        </button>
        
        <button 
          className={`tab-button ${activeTab === 'upload' ? 'active' : ''}`}
          onClick={() => onTabChange('upload')}
        >
          📁 Upload PDF
        </button>
      </nav>
    </header>
  );
}

export default Header;