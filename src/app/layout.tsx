
'use client';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { Toaster } from '@/components/ui/toaster';
import { usePathname } from 'next/navigation';
import AppLayout from '@/components/app-layout';

const inter = Inter({ subsets: ['latin'], variable: '--font-inter' });

// export const metadata: Metadata = {
//   title: 'PharmaFlow',
//   description: 'A Warehouse Management System for pharmaceutical logistics.',
// };

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const pathname = usePathname();
  const isLoginPage = pathname === '/login';

  return (
    <html lang="en">
      <head>
        <title>PharmaFlow</title>
        <meta name="description" content="A Warehouse Management System for pharmaceutical logistics." />
      </head>
      <body className={`${inter.variable} font-sans antialiased`}>
        {isLoginPage ? (
          children
        ) : (
          <AppLayout>
            {children}
          </AppLayout>
        )}
        <Toaster />
      </body>
    </html>
  );
}
