import React from 'react';
import { Check } from 'lucide-react';
import clsx from 'clsx';
import './RuleCard.css';

const RuleCard = ({ rule, isSelected, isFocused, onToggle, onFocus }) => {
    const Icon = rule.icon;

    return (
        <div
            className={clsx('rule-card', {
                selected: isSelected,
                focused: isFocused
            })}
            onClick={onToggle}
            onMouseEnter={onFocus} // Mouse hover also sets focus
            // Setup for potential focus management integration
            tabIndex={0}
            onFocus={onFocus}
            role="button"
            aria-pressed={isSelected}
        >
            {isSelected && (
                <div className="selected-badge">
                    <Check size={16} />
                </div>
            )}

            <Icon
                className="rule-icon"
                style={rule.iconRotate ? { transform: `rotate(${rule.iconRotate}deg)` } : {}}
            />
            <div className="rule-name">{rule.name}</div>
        </div>
    );
};

export default RuleCard;
