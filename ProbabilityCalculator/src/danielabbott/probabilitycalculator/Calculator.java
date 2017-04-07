package danielabbott.probabilitycalculator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Calculator extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator frame = new Calculator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private double getBinomialCoefficient(int n, int r) {
		double coeff = 1.0f;
		for (double i = 1.0; i <= r; i++) {
			coeff *= (n + 1.0 - i) / i;
		}
		return coeff;
	}

	private double calculateProbability(int n, double p, int r) {
		/* nCr * p^r * q^r where q = 1-p */
		return getBinomialCoefficient(n, r) * Math.pow(p, r) * Math.pow(1.0 - p, n - r);
	}

	/* Equation inputs */

	private int n = 1;
	private double p = 0.5f;
	private int r = 0;

	/* GUI Components */

	private JSlider slider_success;
	private JSlider slider_failure;
	private JSlider slider_successful_trials;
	private JTextField inpProbSuccess;
	private JTextField inpProbFail;
	private JTextField inpSuccessfulTrials;
	private JTextField inpTrials;
	private JLabel lblProbOutput;

	private void update() {
		double probability = calculateProbability(n, p, r);
		lblProbOutput.setText(String.format("%.4f  (%.2f %%)", probability, probability * 100.0));
	}
	
	private void updateProbabilities(){
		if (p < 0) {
			p = 0;
			inpProbSuccess.setText("0.0");
			slider_success.setValue(0);
			inpProbFail.setText("1.0");
			slider_failure.setValue(100);
		} else if (p > 1) {
			p = 1;
			inpProbSuccess.setText("1.0");
			slider_success.setValue(100);
			inpProbFail.setText("0.0");
			slider_failure.setValue(0);
		} else {
			slider_failure.setValue((int)Math.round(((1.0 - p) * 100.0)));
			slider_success.setValue((int)Math.round((p * 100.0)));
		}

		update();
	}
	
	/**
	 * @param i Integer in the range 0 - 100 inclusive
	 * @return String representation of integer, e.g. 6 becomes "0.06"
	 */
	private String intToDecimal(int i) {
		if(i < 10)
			return "0.0" + i;
		else
			return (i / 100) + "." + (i % 100);
	}

	/**
	 * Create the frame.
	 */
	public Calculator() {
		setTitle("Probabilty Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 568, 204);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNumberOfTrials = new JLabel("Number of trials:");
		lblNumberOfTrials.setBounds(10, 14, 180, 14);
		contentPane.add(lblNumberOfTrials);

		inpTrials = new JTextField();
		inpTrials.setBounds(269, 11, 86, 20);
		inpTrials.setText("1");
		contentPane.add(inpTrials);
		inpTrials.setColumns(10);

		JLabel lblProbSuccess = new JLabel("Probabiltiy of success:");
		lblProbSuccess.setBounds(10, 42, 180, 14);
		contentPane.add(lblProbSuccess);

		slider_success = new JSlider();
		slider_success.setBounds(212, 36, 200, 26);
		contentPane.add(slider_success);

		inpProbSuccess = new JTextField();
		inpProbSuccess.setBounds(417, 39, 86, 20);
		inpProbSuccess.setText("0.5");
		contentPane.add(inpProbSuccess);
		inpProbSuccess.setColumns(10);

		JLabel lblProbFail = new JLabel("Probability of failure:");
		lblProbFail.setBounds(10, 73, 180, 14);
		contentPane.add(lblProbFail);

		slider_failure = new JSlider();
		slider_failure.setBounds(212, 67, 200, 26);
		contentPane.add(slider_failure);

		inpProbFail = new JTextField();
		inpProbFail.setBounds(417, 70, 86, 20);
		inpProbFail.setText("0.5");
		contentPane.add(inpProbFail);
		inpProbFail.setColumns(10);

		JLabel lblSuccessfulTrials = new JLabel("Number of successful trials:");
		lblSuccessfulTrials.setBounds(10, 104, 180, 14);
		contentPane.add(lblSuccessfulTrials);

		slider_successful_trials = new JSlider();
		slider_successful_trials.setBounds(212, 98, 200, 26);
		slider_successful_trials.setMaximum(1);
		slider_successful_trials.setValue(0);
		contentPane.add(slider_successful_trials);

		inpSuccessfulTrials = new JTextField();
		inpSuccessfulTrials.setBounds(417, 101, 86, 20);
		inpSuccessfulTrials.setText("0");
		contentPane.add(inpSuccessfulTrials);
		inpSuccessfulTrials.setColumns(10);

		JLabel lblProbability = new JLabel("Probability:");
		lblProbability.setBounds(10, 135, 180, 14);
		contentPane.add(lblProbability);

		lblProbOutput = new JLabel("0.5");
		lblProbOutput.setHorizontalAlignment(SwingConstants.CENTER);
		lblProbOutput.setBounds(267, 135, 104, 14);
		contentPane.add(lblProbOutput);

		/* Event handlers */

		slider_success.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = slider_success.getValue();
				int failValue = 100 - value;
				slider_failure.setValue(100 - value);
				p = value / 100.0;
				inpProbSuccess.setText(intToDecimal(value));
				
				inpProbFail.setText(intToDecimal(failValue));
				update();
			}
		});

		slider_failure.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = slider_failure.getValue();
				int successValue = 100 - value;
				slider_success.setValue(100 - value);
				p = (100 - value) / 100.0;
				inpProbFail.setText(intToDecimal(value));
				inpProbSuccess.setText(intToDecimal(successValue));
				update();
			}
		});

		slider_successful_trials.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				r = slider_successful_trials.getValue();
				inpSuccessfulTrials.setText(Integer.toString(r));
				update();
			}
		});

		inpTrials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					n = Integer.parseInt(inpTrials.getText());

					if (n < 1) {
						n = 1;
						inpTrials.setText("1");
					}

					slider_successful_trials.setMaximum(n);

					if (r > n) {
						r = n;
						inpSuccessfulTrials.setText(Integer.toString(r));
					}
					slider_successful_trials.setValue(r);

				} catch (NumberFormatException e2) {
					inpTrials.setText("1");
					n = 1;

					/* 'r' may be invalid as 'n' just changed */
					if (r > 1) {
						r = 0;
						inpSuccessfulTrials.setText("1");
						slider_successful_trials.setValue(1);
					}
				}

				update();
			}
		});


		inpProbSuccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					p = Double.parseDouble(inpProbSuccess.getText());
				} catch (NumberFormatException e2) {
					p = -1;
				}

				updateProbabilities();
			}
		});
		
		inpProbFail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					p = 1.0 - Double.parseDouble(inpProbFail.getText());
				} catch (NumberFormatException e2) {
					p = -1;
				}

				updateProbabilities();
			}
		});
		
		inpSuccessfulTrials.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				try {
					r = Integer.parseInt(inpSuccessfulTrials.getText());
				} catch (NumberFormatException e2) {
					r = -1;
				}

				if (r < 0) {
					r = 0;
					inpSuccessfulTrials.setText("0");
					slider_successful_trials.setValue(0);
				} else if (r > n) {
					r = n;
					inpSuccessfulTrials.setText(Integer.toString(r));
					slider_successful_trials.setValue(r);
				} else {
					slider_successful_trials.setValue(r);
				}

				update();
			}
		});
		
		update();

	}
}
