import React from 'react';
import { X } from 'lucide-react';
import TicketVisualizer from './TicketVisualizer';
import './RuleDetailModal.css';

const RuleDetailModal = ({ rule, isSelected, onDismiss, onToggle }) => {
    if (!rule) return null;

    const handleToggle = () => {
        onToggle(rule.id);
        onDismiss();
    };

    return (
        <div className="modal-overlay" onClick={onDismiss}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <div className="modal-title">{rule.name}</div>
                    <button className="close-btn-icon" onClick={onDismiss}>
                        <X size={24} />
                    </button>
                </div>

                <div className="modal-body">
                    <TicketVisualizer highlighted={rule.winningPattern} />
                    <div className="rule-description">
                        {rule.description}
                    </div>
                </div>

                <div className="modal-footer">
                    <button
                        className={`action-btn ${isSelected ? 'unselect' : 'select'}`}
                        onClick={handleToggle}
                    >
                        {isSelected ? 'Unselect Rule' : 'Select Rule'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RuleDetailModal;
