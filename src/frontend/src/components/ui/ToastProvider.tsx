import { Toaster } from "react-hot-toast";

export default function ToastProvider() {
  return <Toaster position="top-right" toastOptions={{
    success: { className: 'toaster-success' },
    error: { className: 'toaster-error' }
  }} />;
}
