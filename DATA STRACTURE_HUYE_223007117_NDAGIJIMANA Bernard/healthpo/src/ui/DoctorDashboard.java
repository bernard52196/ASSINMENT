package ui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorDashboard extends JFrame {
    private final User user;
    private final Doctor doctor;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    public DoctorDashboard(User user, Doctor doctor) {
        this.user = user;
        this.doctor = doctor;

        setTitle("Doctor Dashboard - " + (user != null ? user.getFullName() : ""));
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==== TOP PANEL ====
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(1000, 70));

        JLabel title = new JLabel("DOCTOR CONTROL PANEL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(20, 15, 400, 40);
        topPanel.add(title);

        JButton btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setBackground(new Color(220, 54, 54));
        btnBackToLogin.setForeground(Color.WHITE);
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBackToLogin.setBounds(820, 15, 150, 35);

        btnBackToLogin.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to log out and go back to login?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        topPanel.add(btnBackToLogin);
        add(topPanel, BorderLayout.NORTH);

        // ==== MAIN TABS ====
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Appointments", appointmentsPanel());
        tabs.addTab("Medical Records", medicalRecordsPanel());
        tabs.addTab("Prescriptions", prescriptionsPanel());
        add(tabs, BorderLayout.CENTER);
    }

    // ===================== APPOINTMENTS PANEL =====================
    private JPanel appointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"AppointmentID", "PatientID", "Date", "Status", "Notes"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton approve = new JButton("Approve");
        JButton cancel = new JButton("Cancel");
        JButton complete = new JButton("Complete");
        JButton refresh = new JButton("Refresh");

        bottom.add(approve);
        bottom.add(cancel);
        bottom.add(complete);
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Appointment> list = appointmentDAO.getByDoctor(doctor.getDoctorID());
            for (Appointment a : list)
                model.addRow(new Object[]{
                        a.getAppointmentID(),
                        a.getPatientID(),
                        a.getAppointmentDate(),
                        a.getStatus(),
                        a.getNotes()
                });
        });

        approve.addActionListener(e -> changeStatus(table, model, "Approved"));
        cancel.addActionListener(e -> changeStatus(table, model, "Cancelled"));
        complete.addActionListener(e -> changeStatus(table, model, "Completed"));

        refresh.doClick();
        return panel;
    }

    private void changeStatus(JTable table, DefaultTableModel model, String status) {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select appointment");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(r, 0).toString());
        appointmentDAO.updateStatus(id, status);
        model.setValueAt(status, r, 3);
    }

    // ===================== MEDICAL RECORDS PANEL =====================
    private JPanel medicalRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"RecordID", "Attribute1", "Attribute2", "Attribute3", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add");
        JPanel bottom = new JPanel();
        bottom.add(add);
        bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<MedicalRecord> list = recordDAO.getAll();
            for (MedicalRecord m : list)
                model.addRow(new Object[]{
                        m.getMedicalRecordID(),
                        m.getAttribute1(),
                        m.getAttribute2(),
                        m.getAttribute3(),
                        m.getCreatedAt()
                });
        });

        add.addActionListener(e -> {
            JTextField a1 = new JTextField();
            JTextField a2 = new JTextField();
            JTextField a3 = new JTextField();
            Object[] form = {"Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                MedicalRecord m = new MedicalRecord();
                m.setAttribute1(a1.getText().trim());
                m.setAttribute2(a2.getText().trim());
                m.setAttribute3(a3.getText().trim());
                recordDAO.add(m);
                refresh.doClick();
            }
        });

        refresh.doClick();
        return panel;
    }

    // ===================== PRESCRIPTIONS PANEL =====================
    private JPanel prescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"PrescriptionID", "AppointmentID", "Attribute1", "Attribute2", "Attribute3", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Prescription");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            for (Prescription p : prescriptionDAO.getAll()) {
                model.addRow(new Object[]{
                        p.getPrescriptionID(),
                        p.getAppointmentID(),
                        p.getAttribute1(),
                        p.getAttribute2(),
                        p.getAttribute3(),
                        p.getCreatedAt()
                });
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField apptId = new JTextField();
            JTextField a1 = new JTextField();
            JTextField a2 = new JTextField();
            JTextField a3 = new JTextField();
            Object[] form = {"AppointmentID:", apptId, "Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Add Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Prescription p = new Prescription();
                    p.setAppointmentID(Integer.parseInt(apptId.getText().trim()));
                    p.setAttribute1(a1.getText().trim());
                    p.setAttribute2(a2.getText().trim());
                    p.setAttribute3(a3.getText().trim());
                    prescriptionDAO.add(p);
                    JOptionPane.showMessageDialog(this, "Prescription added!");
                    btnRefresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid data.");
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a prescription.");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            JTextField a1 = new JTextField(model.getValueAt(r, 2).toString());
            JTextField a2 = new JTextField(model.getValueAt(r, 3).toString());
            JTextField a3 = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {"Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Update Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Prescription p = new Prescription();
                p.setPrescriptionID(id);
                p.setAttribute1(a1.getText().trim());
                p.setAttribute2(a2.getText().trim());
                p.setAttribute3(a3.getText().trim());
                prescriptionDAO.update(p);
                JOptionPane.showMessageDialog(this, "Updated successfully.");
                btnRefresh.doClick();
            }
        });

        btnDelete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select prescription to delete.");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            prescriptionDAO.delete(id);
            JOptionPane.showMessageDialog(this, "Deleted!");
            btnRefresh.doClick();
        });

        btnRefresh.doClick();
        return panel;
    }
}
