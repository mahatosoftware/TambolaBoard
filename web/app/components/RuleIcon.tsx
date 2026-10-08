import React from "react";

interface RuleIconProps {
  type: string;
  className?: string;
  size?: number;
}

export const RuleIcon: React.FC<RuleIconProps> = ({ type, className = "", size = 48 }) => {
  switch (type) {
    case "FULL_HOUSE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="2" y="2" width="20" height="20" rx="4" fill="#4CAF50" />
          <path d="M6 12h12M6 8h12M6 16h12" stroke="#FFFFFF" strokeWidth="1.5" strokeLinecap="round" />
          <path d="M10 6v12M14 6v12" stroke="#FFFFFF" strokeWidth="1.5" strokeLinecap="round" />
          <circle cx="18" cy="6" r="3.5" fill="#FFD700" />
          <path d="M16.8 6l1 1 2-2" stroke="#000000" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      );

    case "SECOND_HOUSE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 2L2 10v11h20V10L12 2z" fill="#2196F3" />
          <path d="M12 2L1 10.5h3L12 4l8 6.5h3L12 2z" fill="#1565C0" />
          <path d="M6 13h12M6 17h12M10 10v9M14 10v9" stroke="#FFFFFF" strokeWidth="1" />
          <circle cx="18" cy="13.5" r="3.5" fill="#FFC107" />
          <path d="M16.5 12.2c0-.7.6-1.2 1.5-1.2s1.5.5 1.5 1.2c0 .6-.4 1-.1.3l-1.7 1.4v.7h3.3v-1h-2l1-.8c.8-.7 1.2-1.3 1.2-2 0-1.2-.9-2-2.1-2-1.1 0-2 .8-2 2h1.2z" fill="#212121" />
        </svg>
      );

    case "THIRD_HOUSE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 2L2 10v11h20V10L12 2z" fill="#9C27B0" />
          <path d="M12 2L1 10.5h3L12 4l8 6.5h3L12 2z" fill="#6A1B9A" />
          <path d="M6 13h12M6 17h12M10 10v9M14 10v9" stroke="#FFFFFF" strokeWidth="1" />
          <circle cx="18" cy="13.5" r="3.5" fill="#FF9800" />
          <path d="M16.5 11h3v1l-1.5 1.2c.8.1 1.5.6 1.5 1.5 0 .9-.8 1.5-1.7 1.5-1 0-1.6-.7-1.6-1.6h1c0 .5.3.8.6.8.4 0 .7-.3.7-.8 0-.5-.4-.8-1-.8h-.5v-.8l1.3-1.2h-1.8V11z" fill="#212121" />
        </svg>
      );

    case "EARLY_FIVE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 2l3 7h7l-5.5 5 2 7-6.5-4-6.5 4 2-7L2 9h7z" fill="#FFC107" />
          <circle cx="12" cy="12" r="4.5" fill="#FFA000" />
          <text x="12" y="15.5" fontSize="10" fontWeight="bold" textAnchor="middle" fill="#FFFFFF" fontFamily="sans-serif">5</text>
        </svg>
      );

    case "TOP_LINE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="4" width="18" height="4" rx="1" fill="#2196F3" />
          <rect x="3" y="10" width="18" height="4" rx="1" fill="#BDBDBD" />
          <rect x="3" y="16" width="18" height="4" rx="1" fill="#BDBDBD" />
        </svg>
      );

    case "MIDDLE_LINE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="4" width="18" height="4" rx="1" fill="#BDBDBD" />
          <rect x="3" y="10" width="18" height="4" rx="1" fill="#2196F3" />
          <rect x="3" y="16" width="18" height="4" rx="1" fill="#BDBDBD" />
        </svg>
      );

    case "BOTTOM_LINE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="4" width="18" height="4" rx="1" fill="#BDBDBD" />
          <rect x="3" y="10" width="18" height="4" rx="1" fill="#BDBDBD" />
          <rect x="3" y="16" width="18" height="4" rx="1" fill="#2196F3" />
        </svg>
      );

    case "CORNER":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="3" width="5" height="5" rx="1" fill="#FF5722" />
          <rect x="16" y="3" width="5" height="5" rx="1" fill="#FF5722" />
          <rect x="3" y="16" width="5" height="5" rx="1" fill="#FF5722" />
          <rect x="16" y="16" width="5" height="5" rx="1" fill="#FF5722" />
          <rect x="9.5" y="9.5" width="5" height="5" rx="1" fill="none" stroke="#BDBDBD" strokeWidth="1" strokeDasharray="1 1" />
        </svg>
      );

    case "DIAMOND":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 2l4 4-4 4-4-4z" fill="#03A9F4" />
          <path d="M12 14l4 4-4 4-4-4z" fill="#FF9800" />
          <path d="M2 12l4-4 4 4-4 4z" fill="#4CAF50" />
          <path d="M14 12l4-4 4 4-4 4z" fill="#9C27B0" />
          <path d="M8 12l4-4 4 4-4 4z" fill="#2196F3" />
        </svg>
      );

    case "PYRAMID":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 3L3 20h18L12 3z" fill="#9C27B0" />
          <path d="M12 7l-5 9h10l-5-9z" fill="#E1BEE7" opacity="0.6" />
        </svg>
      );

    case "INVERTED_PYRAMID":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M3 4h18L12 21 3 4z" fill="#9C27B0" />
          <path d="M7 8h10l-5 9-5-9z" fill="#E1BEE7" opacity="0.6" />
        </svg>
      );

    case "STAR":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <path d="M12 1.5l3.3 6.7 7.4 1.1-5.4 5.2 1.3 7.3L12 18.3 5.4 21.8l1.3-7.3L1.3 9.3l7.4-1.1L12 1.5z" fill="#FFC107" />
          <path d="M12 4l2.2 4.5 4.9.7-3.5 3.5.8 4.9-4.4-2.4z" fill="#FFA000" />
          <circle cx="12" cy="12" r="2" fill="#FFFFFF" />
        </svg>
      );

    case "ODDS":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="2" y="2" width="20" height="20" rx="4" fill="#3F51B5" />
          <text x="5" y="16" fontSize="11" fontWeight="bold" fill="#FFFFFF" fontFamily="sans-serif">1</text>
          <text x="10.5" y="16" fontSize="11" fontWeight="bold" fill="#FFC107" fontFamily="sans-serif">3</text>
          <text x="16" y="16" fontSize="11" fontWeight="bold" fill="#FFFFFF" fontFamily="sans-serif">5</text>
        </svg>
      );

    case "EVEN":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="2" y="2" width="20" height="20" rx="4" fill="#009688" />
          <text x="5" y="16" fontSize="11" fontWeight="bold" fill="#FFFFFF" fontFamily="sans-serif">2</text>
          <text x="10.5" y="16" fontSize="11" fontWeight="bold" fill="#FFD700" fontFamily="sans-serif">4</text>
          <text x="16" y="16" fontSize="11" fontWeight="bold" fill="#FFFFFF" fontFamily="sans-serif">6</text>
        </svg>
      );

    case "FIRST_HALF":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="4" width="8" height="16" rx="2" fill="#03A9F4" />
          <rect x="13" y="4" width="8" height="16" rx="2" fill="#BDBDBD" />
        </svg>
      );

    case "SECOND_HALF":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="3" y="4" width="8" height="16" rx="2" fill="#BDBDBD" />
          <rect x="13" y="4" width="8" height="16" rx="2" fill="#03A9F4" />
        </svg>
      );

    case "BREAKFAST":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <circle cx="12" cy="12" r="10" fill="#FFECB3" />
          <circle cx="12" cy="12" r="8" fill="#FFFFFF" />
          <path d="M12 12H6M12 6v6" stroke="#FF9800" strokeWidth="2" strokeLinecap="round" />
          <circle cx="16" cy="16" r="3" fill="#FFC107" />
        </svg>
      );

    case "LUNCH":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <circle cx="12" cy="12" r="10" fill="#E0E0E0" />
          <circle cx="12" cy="12" r="8" fill="#FFFFFF" />
          <path d="M12 12l5-4M12 6v6" stroke="#424242" strokeWidth="2" strokeLinecap="round" />
          <rect x="14" y="14" width="8" height="6" rx="1" fill="#FF9800" />
          <text x="18" y="18.5" fontSize="5" fontWeight="bold" textAnchor="middle" fill="#FFFFFF" fontFamily="sans-serif">PM</text>
        </svg>
      );

    case "DINNER":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <circle cx="12" cy="12" r="10" fill="#263238" />
          <circle cx="12" cy="12" r="8" fill="#37474F" />
          <path d="M12 12H6M12 6v6" stroke="#ECEFF1" strokeWidth="2" strokeLinecap="round" />
          <path d="M17 14a3 3 0 1 1-3 3 2 2 0 1 0 3-3z" fill="#FFEB3B" />
        </svg>
      );

    case "TEMPERATURE":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="10" y="3" width="4" height="12" rx="2" fill="#F44336" />
          <circle cx="12" cy="17" r="4" fill="#F44336" />
          <circle cx="12" cy="17" r="2" fill="#FFFFFF" />
          <path d="M12 6v7" stroke="#FFFFFF" strokeWidth="1" strokeLinecap="round" />
        </svg>
      );

    case "BELOW_FIFTY":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="2" y="2" width="20" height="20" rx="4" fill="#00BCD4" />
          <path d="M12 5v9M8 10l4 4 4-4" stroke="#FFFFFF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          <text x="12" y="20.5" fontSize="5.5" fontWeight="bold" textAnchor="middle" fill="#FFFFFF" fontFamily="sans-serif">&lt;50</text>
        </svg>
      );

    case "ABOVE_FIFTY":
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <rect x="2" y="2" width="20" height="20" rx="4" fill="#FF5722" />
          <path d="M12 19v-9M8 14l4-4 4 4" stroke="#FFFFFF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          <text x="12" y="7" fontSize="5.5" fontWeight="bold" textAnchor="middle" fill="#FFFFFF" fontFamily="sans-serif">&gt;50</text>
        </svg>
      );

    default:
      return (
        <svg width={size} height={size} viewBox="0 0 24 24" fill="none" className={className}>
          <circle cx="12" cy="12" r="10" fill="#FF9800" />
          <text x="12" y="16" fontSize="12" fontWeight="bold" textAnchor="middle" fill="#FFFFFF">🎯</text>
        </svg>
      );
  }
};
