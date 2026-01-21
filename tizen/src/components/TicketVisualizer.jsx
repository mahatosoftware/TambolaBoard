import React from 'react';
import clsx from 'clsx';
import './TicketVisualizer.css';

const TicketVisualizer = ({ highlighted = [] }) => {
    // Fixed Tambola ticket (3 x 9)
    const ticket = [
        [1, null, 23, null, 41, 52, null, 78, null],
        [null, 15, null, 34, null, 56, 63, null, 82],
        [9, null, 27, 38, null, null, null, 74, 90]
    ];

    return (
        <div className="ticket-visualizer">
            <div className="ticket-header">Tambola Board</div>

            <div className="ticket-grid">
                {ticket.map((row, rowIndex) => (
                    <div key={rowIndex} className="ticket-row">
                        {row.map((number, colIndex) => {
                            const index = rowIndex * 9 + colIndex;
                            const isActive = highlighted.includes(index) && number !== null;
                            const isEmpty = number === null;

                            return (
                                <div
                                    key={colIndex}
                                    className={clsx('ticket-cell', {
                                        'active': isActive,
                                        'empty': isEmpty,
                                        'has-number': !isEmpty
                                    })}
                                >
                                    {number}
                                </div>
                            );
                        })}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default TicketVisualizer;
